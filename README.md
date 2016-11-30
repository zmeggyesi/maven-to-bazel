# maven-to-bazel
## Maven-Bazel dependency migrator

Assist migrating from Maven to Bazel by reading the `pom.xml` for dependencies and outputting a formatted set of Maven directives for the `WORKSPACE` file.

## Switches

N.B.: All switches are VM Arguments! Both the `pom` and the `outputFilePrefix` options are passed to `File` constructors, so full paths can be supplied.

- `pom`: Location of the POM file for processing.
- `outputFilePrefix`: Determines the prefix of the output files. If set, console output is suppressed and directed to the selected files.
- `outputDirectives`: Instructs the Migrator to output the `WORKSPACE` directives into a file named `${outputFilePrefix}-directives`
- `outputReferences`: Instructs the Migrator to output the `BUILD` directives into a file named `${outputFilePrefix}-references`
