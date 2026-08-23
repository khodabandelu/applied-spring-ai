# Contributing to Applied Spring AI

Thank you for helping improve Applied Spring AI. Contributions should keep the repository practical, focused, and easy to run locally.

## Before you start

- Use Java 25.
- Never commit API keys or other credentials.
- Keep each application focused on one primary Spring AI capability.
- Prefer deterministic automated tests that do not require a live provider.
- Use model evaluations, rather than exact-string assertions, for probabilistic output quality.

For substantial new examples or structural changes, open an issue first so the scope can be discussed before implementation.

## Local validation

For a live local provider call, copy the safe template and edit the ignored local file:

```bash
cp config/local/set-env.example.sh config/local/set-env.sh
chmod 600 config/local/set-env.sh
# Replace the placeholder in config/local/set-env.sh
source ./config/local/set-env.sh
```

The regular automated tests do not need this file or a real provider credential.

Run the complete repository test suite from the repository root:

```bash
./gradlew test
```

Run a single application test suite with its Gradle project path:

```bash
./gradlew :examples:prompt-api:test
```

## Adding an example

1. Create a capability-based directory under `examples/`.
2. Give the application its own `build.gradle`, source tree, README, and tests.
3. Register the module in the root `settings.gradle`.
4. Add the example to the table and planned-topic list in the root README.
5. Update the Postman collection when the example exposes or changes HTTP endpoints.
6. Confirm that `./gradlew test` succeeds without provider credentials.

Use descriptive capability names such as `structured-output`, `conversation-memory`, or `tool-calling`, and keep paths stable as the repository grows.

When changing Postman resources, connect the repository root in Local View. Keep the importable files under `api-client/postman/` and the repository-level Native Git files synchronized; do not create a nested Postman workspace inside `api-client/postman/`.

## Pull requests

Keep pull requests limited to one clear change. In the description, explain:

- the problem or capability being addressed;
- the important design decisions;
- how the change was tested; and
- whether it changes configuration, API contracts, or provider behavior.

Update documentation whenever code examples, commands, dependencies, or configuration properties change.
