# Moldova Filling Station Report-Aggregator

<p>This module allows to aggregate Jacoco test coverage report data across all modules.</p>

- [Usage](#usage)

# Usage

To generate a Jacoco test coverage report, follow these steps:
1. Run command: `mvn clean verify` from root package.
2. Check required data:
   - HTML report path: `target/site/jacoco-aggregate/index.html`.
   - Execution (`.exec`) report merged file: `target/jacoco-output/merged.exec`.