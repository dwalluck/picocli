/*
   Copyright 2017 Remko Popma

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package picocli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * Tests the scripts generated by BashCompletionAutoComplete.
 */
public class BashCompletionAutoCompleteTest {
    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testBashCompletionAutoComplete() throws Exception {
        File dir = temp.newFolder();
        File completionScript = new File(dir, "test-app_completion");

        BashCompletionAutoComplete.main(String.format("-o=%s", completionScript.getAbsolutePath()), "picocli.BashCompletionAutoCompleteTestApp");

        byte[] completion = Files.readAllBytes(completionScript.toPath());

        System.out.println(new String(completion, "UTF-8"));

        String expected = "# test-app completion                                      -*- shell-script -*-\n" +
                "\n" +
                "_test_app()\n" +
                "{\n" +
                "    local cur prev words cword split\n" +
                "    _init_completion -s -n = || return\n" +
                "\n" +
                "    local commands options\n" +
                "    commands='foo bar'\n" +
                "    options='-h --help -V --version -i --interface'\n" +
                "\n" +
                "    case $prev in\n" +
                "        -i|--interface)\n" +
                "            _available_interfaces\n" +
                "            return\n" +
                "            ;;\n" +
                "    esac\n" +
                "\n" +
                "    $split = && return\n" +
                "\n" +
                "    local arg\n" +
                "    _get_first_arg\n" +
                "\n" +
                "    case $arg in\n" +
                "        foo)\n" +
                "            _test_app_foo\n" +
                "            return\n" +
                "            ;;\n" +
                "        bar)\n" +
                "            _test_app_bar\n" +
                "            return\n" +
                "            ;;\n" +
                "    esac\n" +
                "\n" +
                "    if [[ \"$cur\" == -* ]]; then\n" +
                "        COMPREPLY=( $(compgen -W '${options}' -- \"$cur\" ) )\n" +
                "        [[ $COMPREPLY == *= ]] && compopt -o nospace\n" +
                "    else\n" +
                "        local args\n" +
                "        _count_args =\n" +
                "\n" +
                "        if (( $args == 1 )); then\n" +
                "            COMPREPLY=( $(compgen -W '${commands}' -- \"$cur\" ) )\n" +
                "        fi\n" +
                "\n" +
                "        if (( $args >= 1 )); then\n" +
                "            _filedir -d\n" +
                "            return\n" +
                "        fi\n" +
                "    fi\n" +
                "}\n" +
                "\n" +
                "_test_app_foo()\n" +
                "{\n" +
                "    local cur prev words cword split\n" +
                "    _init_completion -s -n = || return\n" +
                "\n" +
                "    local commands options\n" +
                "    commands=''\n" +
                "    options='-f --foo1'\n" +
                "\n" +
                "    if [[ \"$cur\" == -* ]]; then\n" +
                "        COMPREPLY=( $(compgen -W '${options}' -- \"$cur\" ) )\n" +
                "        [[ $COMPREPLY == *= ]] && compopt -o nospace\n" +
                "    else\n" +
                "        local args\n" +
                "        _count_args =\n" +
                "\n" +
                "        if (( $args == 2 )); then\n" +
                "            _known_hosts_real \"$cur\"\n" +
                "            return\n" +
                "        fi\n" +
                "\n" +
                "        if (( $args == 3 )); then\n" +
                "            _known_hosts_real \"$cur\"\n" +
                "            return\n" +
                "        fi\n" +
                "    fi\n" +
                "}\n" +
                "\n" +
                "_test_app_bar()\n" +
                "{\n" +
                "    local cur prev words cword split\n" +
                "    _init_completion -s -n = || return\n" +
                "\n" +
                "    local commands options\n" +
                "    commands=''\n" +
                "    options='-b --bar1'\n" +
                "\n" +
                "    local bar_parameter_args\n" +
                "    bar_parameter_args=\"foo bar baz\"\n" +
                "\n" +
                "    if [[ \"$cur\" == -* ]]; then\n" +
                "        COMPREPLY=( $(compgen -W '${options}' -- \"$cur\" ) )\n" +
                "        [[ $COMPREPLY == *= ]] && compopt -o nospace\n" +
                "    else\n" +
                "        local args\n" +
                "        _count_args =\n" +
                "\n" +
                "        if (( $args >= 2 && $args <= 3 )); then\n" +
                "            COMPREPLY=( $(compgen -W \"${bar_parameter_args}\" -- \"$cur\" ) )\n" +
                "            return\n" +
                "        fi\n" +
                "    fi\n" +
                "} &&\n" +
                "complete -F _test_app test-app\n" +
                "\n" +
                "# ex: filetype=sh\n" +
                "";

        assertEquals(expected, new String(completion, "UTF8"));
    }

}
