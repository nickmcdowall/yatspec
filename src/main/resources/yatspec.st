<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>LSD</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js" type="text/javascript"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css" type="text/css" media="all"/>
        <link rel="stylesheet" href="https://static.jquery.com/ui/css/demo-docs-theme/ui.theme.css" type="text/css" media="all"/>
        <link rel="stylesheet" id="theme" type="text/css" href="yatspec_alt.css">
    </head>
    <body>
        <div class="hd-switch">
            <input type="checkbox" id="switch-id" name="myswitch" onchange="toggleTheme()">
            <label for="switch-id">Classic</label>
            <span class="hd-switch-knob"></span>
        </div>
        <div class="logo">
            <h5 class="logo1">L</h5>iving &nbsp;<h5 class="logo2">S</h5>equence &nbsp;<h5 class="logo3">D</h5>iagrams
        </div>

        <header>
            <h1>$testResult.name$</h1>
            $testResult.annotations:{ annotation |
            <pre class="notes">$annotation$</pre>
            }$
            <nav class="contents">
                <h2>Contents</h2>
                <ul class="contents">
                    $testResult.testMethods:{ testMethod |
                        <li class='$cssClass.(testMethod.status)$'>
                            <a href="#$testMethod.name$">$testMethod.displayName$</a>
                        </li>
                    }$
                </ul>
            </nav>
        </header>
        <main role="main">
        $testResult.testMethods:{ testMethod |
            <article class="testmethod section">
                <header>
                    <a id="$testMethod.name$"></a>
                    <h2 class="section-header">$testMethod.displayName$</h2>
                </header>
                <section class="section-body">
                        $testMethod.annotations:{ annotation |
                            <pre class="notes">$annotation$</pre>
                        }$
                        $if(!testMethod.scenarioTable.empty)$
                        <section>
                            <h3>Scenarios</h3>
                            <table class="scenarios">
                                <thead>
                                <tr>
                                    $testMethod.scenarioTable.headers: { header | <th>$header$</th>}$
                                </tr>
                                </thead>
                                <tbody>
                                $testMethod.scenarios, testMethod.scenarioTable.rows: { scenario, row |
                                    <tr>
                                        $row: { value |
                                            <td class='$cssClass.(scenario.status)$'><a href="#$scenario.name$">
                                                $if(value.empty)$
                                                    (empty)
                                                $else$
                                                    $value$
                                                $endif$
                                            </a></td>}$
                                    </tr>
                                 }$
                                </tbody>
                            </table>
                        </section>
                        $endif$
                        $testMethod.scenarios: { scenario |
                        <section class="scenario" id="$testMethod.uid$_$scenario.uid$">
                            <section class="specification">
                                <h2>Specification<a id="$scenario.name$"></a></h2>
                                <pre class="highlight specification">$scenario.specification$</pre>
                            </section>
                            <section class="results">
                                <h3 class="logKey">Test result:</h3>
                                <pre class='highlight results $cssClass.(scenario.status)$'>$scenario.message$</pre>
                            </section>
                            $if(scenario.diagram)$
                            <section class="diagram">
                                <h3 logkey="Sequence_Diagram">Sequence Diagram</h3>
                                <div class="highlight SvgWrapper" >$scenario.diagram$</div>
                            </section>
                            $endif$
                            $if(!scenario.interestingGivens.keys.empty)$
                            <section class="interestingGivens">
                                <h3 class="logKey">Interesting Givens</h3>
                                <table class="interestingGivens">
                                $scenario.interestingGivens.keys:{ key |
                                    <tr>
                                        <th class="key">$key$</th>
                                        <td class="interestingGiven">$scenario.interestingGivens.(key)$</td>
                                    </tr>
                                }$
                                </table>
                            </section>
                            $endif$
                            <footer>
                                <section class="interactions">
                                <details>
                                    <summary>Captured Interactions</summary>
                                    $scenario.logs.keys:{ key |
                                    <details class="interaction">
                                        <summary class="logKey" logkey="$key.valueWithSpacesReplacedByUnderscore$">$key.value$</summary>
                                        <div contenteditable="true" ondblclick="document.execCommand('selectAll',false,null)"
                                             class="logValue highlight $scenario.logs.(key).class.simpleName$">$scenario.logs.(key)$</div>
                                    </details>
                                    }$
                                </details>
                            </section>
                            </footer>
                        </section>
                        }$
                </section>
            </article>
            }$
        </main>
        <footer>
            <div class="footer">
                <script src="xregexp.js" type="text/javascript"></script>
                <script src="yatspec.js" type="text/javascript"></script>
            </div>
        </footer>
    </body>
</html>
