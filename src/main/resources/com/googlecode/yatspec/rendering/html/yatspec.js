// Add support for escape sequences: \Q⋯\E and \Q⋯
XRegExp.addToken(
    /\\Q([\s\S]*?)(?:\\E|$)/,
    function (match) {return XRegExp.escape(match[1])},
    XRegExp.INSIDE_CLASS | XRegExp.OUTSIDE_CLASS
);

RegExp.prototype.replace = function(str, replacer, nonMatchedReplacer) {
    nonMatchedReplacer = nonMatchedReplacer || function(value) {
        return value;
    };
    var result = [];

    var position = 0;
    var match;
    while (( match = this.exec(str)) != null) {
        result.push(nonMatchedReplacer(str.substring(position, match.index)));
        result.push(replacer(match));
        position = this.lastIndex;
    }
    result.push(nonMatchedReplacer(str.substring(position)));

    return result.join("");
}

function yatspec() {
}

yatspec.regex = function(expression, flags) {
    var result =  new XRegExp(expression, flags);
    result.replace = RegExp.prototype.replace;
    return result;
}

yatspec.processed = 'highlighted';

yatspec.highlight = function(element, pairs) {
    if (pairs.length == 0) {
        return;
    }

    if ($(element).children(".nohighlight").length > 0) {
        return;
    }

    if ($(element).hasClass(yatspec.processed)) {
        return;
    }

    var classes = [];
    var matchGroups = [];
    $.each(pairs, function() {
        matchGroups.push("(", this.pattern, ")", "|");
        classes.push(this.cssClass);
    });
    matchGroups.pop();
    var regex = yatspec.regex(matchGroups.join(""), "g");

    $(element).html(regex.replace($(element).html(), function(match) {
        var matches = match.slice(1);
        for (var i = 0; i < matches.length; i++) {
            if (matches[i]) {
                return '<span class="' + classes[i] + '">' + matches[i] + '</span>'
            }
        }
    }));

    $(element).addClass(yatspec.processed);
}

$(document).ready(function () {
    var specificationHighlights = [
        {pattern: '"[^"]*"',     cssClass: "quote" },
        {pattern: "Given",       cssClass: "keyword" },
        {pattern: "And",         cssClass: "keyword" },
        {pattern: "When",        cssClass: "keyword" },
        {pattern: "Then",        cssClass: "keyword" },
        {pattern: "[A-Z_]{2,}" , cssClass: "constant" },
        {pattern: "[\\d]+" ,     cssClass: "literal" }
    ];

    if (yatspec.additionalSpecificationHighlights) {
        $.merge(specificationHighlights, yatspec.additionalSpecificationHighlights);
    }

    $('.highlight.specification').each(function() {
        yatspec.highlight(this, specificationHighlights);
    })

    $('.highlight.results').each(function() {
        yatspec.highlight(this, [
            {pattern: '"[^"]*"',     cssClass: "quote" },
            {pattern: "Expected",    cssClass: "keyword" },
            {pattern: "got",         cssClass: "keyword" },
            {pattern: "[A-Z_]{2,}" , cssClass: "constant" },
            {pattern: "[\\d]+" ,     cssClass: "literal" }
        ]);
    })

    $('.scenario').each(function() {
        var escapeRegExp = function(str) {
            return str.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1");
        };

        var interestingGivens = $('.interestingGiven', this).filter(':not(:empty)').map(
            function() {
                return [
                    {pattern: '\\Q"' + $(this).text() + '"\\E',     cssClass: "interestingGiven" },
                    {pattern: '\\Q' + $(this).text() + "\\E", cssClass: "interestingGiven" },
                ];
            }).get();

        $('.logKey', this).each(function() {
            $(this).next('.logValue.highlight').each(function() {
                yatspec.highlight(this, interestingGivens.concat([
                    {pattern: '"[^"]*"',      cssClass: "quote" },
                    {pattern: "&lt;[^\\s&]+", cssClass: "keyword" },
                    {pattern: "\\??&gt;",     cssClass: "keyword" },
                    {pattern: "\\s[\\w:-]+=", cssClass: "constant" }
                ]));
            })
        });
    })

}, false);

function toggleTheme() {
    var el = document.getElementById("theme");
    if (el.href.match("yatspec_classic.css")) {
        el.href = "yatspec_alt.css";
    } else {
        el.href = "yatspec_classic.css";
    }
}

var createDialogsForSequenceDiagramMessages = function() {
    var messagePayloadDialogs = {};
    $(".sequence_diagram_clickable").each(function() {
        var sequenceDiagramMessage = $(this);
        var myScenario = sequenceDiagramMessage.parents(".scenario");
        var scenarioUid = myScenario.attr("id");
        var sequenceDiagramMessageId = sequenceDiagramMessage.attr("sequence_diagram_message_id");

        var dialogContent = myScenario.find('summary[logKey="' + sequenceDiagramMessageId + '"]').next("div").clone();

        var capturedInputAndOutputsName = sequenceDiagramMessage.text();
        sequenceDiagramMessageId = "" + sequenceDiagramMessageId + "_" + scenarioUid;
        messagePayloadDialogs[sequenceDiagramMessageId] = dialogContent.dialog({title:capturedInputAndOutputsName,minWidth: 800,stack:false,closeOnEscape:true, autoOpen:false });
    });
    return messagePayloadDialogs;
};

var dialogsCreated = false;
var messagePayloadDialogs = {};
$(document).ready(function () {
    $(".sequence_diagram_clickable").click(function() {
        openDialog($(this), "click");
    });

    $(".sequence_diagram_clickable").hover(function() {
        openDialog($(this), "hover");
    }, function(){
        closeHoveredDialog($(this));
    })
});

function dialogForSequenceDiagramMessage(sequenceDiagramMessage) {
    var myScenario = sequenceDiagramMessage.parents(".scenario");
    var scenarioUid = myScenario.attr("id");
    var sequenceDiagramMessageId = "" + sequenceDiagramMessage.attr("sequence_diagram_message_id") + "_" + scenarioUid;
    return messagePayloadDialogs[sequenceDiagramMessageId];
}

function closeHoveredDialog(sequenceDiagramMessage) {
    var dialog = dialogForSequenceDiagramMessage(sequenceDiagramMessage);
    if(dialog.openMethod == "hover" ) {
        dialog.dialog("close")
    }
}

function openDialog(sequenceDiagramMessage, openMethod) {
    if (!dialogsCreated) {
        messagePayloadDialogs = createDialogsForSequenceDiagramMessages();
        dialogsCreated = true;
    }

    var dialog = dialogForSequenceDiagramMessage(sequenceDiagramMessage);
    dialog.openMethod=openMethod;
    dialog.dialog("open");
}

