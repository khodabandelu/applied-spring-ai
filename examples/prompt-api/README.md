# Prompt API

A focused Spring Boot application that submits prompts through Spring AI's `ChatClient` and returns model responses as JSON.

## API contract

```http
POST /api/prompts
Content-Type: application/json
```

Request:

```json
{
  "prompt": "Explain why idempotency matters in distributed systems in one sentence."
}
```

Response:

```json
{
  "response": "Idempotency allows repeated operations to produce the same effect, making retries safe when network failures occur."
}
```

Successful requests return HTTP `201 Created`. An empty or whitespace-only prompt is rejected before a model call is made.

## Prerequisites

- Java 25
- An OpenAI API key only when running against the live provider

The repository includes one Gradle Wrapper at its root, so a separate Gradle installation is not required.

## Test the application

From the repository root:

```bash
./gradlew :examples:prompt-api:test
```

The contract test starts WireMock as a deterministic provider substitute. It verifies the provider path, selected model, user message, response parsing, and application mapping without calling OpenAI.

To test every application in the repository:

```bash
./gradlew test
```

## Run against OpenAI

From the repository root:

```bash
cp config/local/set-env.example.sh config/local/set-env.sh # first setup only
# Replace the placeholder in config/local/set-env.sh
source ./config/local/set-env.sh
./gradlew :examples:prompt-api:bootRun
```

The complete `config/local/set-env.sh` file is ignored by Git. Do not run the copy command again after adding your real key because it would overwrite the local file. See the repository's [configuration guide](../../config/README.md).

Then submit a prompt from another terminal:

```bash
curl --fail-with-body http://localhost:8080/api/prompts \
  -H 'Content-Type: application/json' \
  -d '{"prompt":"Explain why idempotency matters in distributed systems in one sentence."}'
```

## Check application health

Actuator exposes only the health endpoint over HTTP:

```bash
curl --fail-with-body http://localhost:8080/actuator/health
```

A healthy application returns `200 OK` with `status` set to `UP`. Spring Boot may also list the `liveness` and `readiness` group names, while component details remain hidden.

## Try with Postman

Import the repository's [Postman collection](../../api-client/postman) to run the prompt, validation-error, and health-check scenarios with saved response examples and automated contract checks.

## Request flow

```text
POST /api/prompts
    -> PromptController
    -> PromptService
    -> SpringAiPromptService
    -> Spring AI ChatClient
    -> OpenAI
```

The controller does not know which provider or model is used. Those details remain behind the application service and external configuration.

## Configuration

The model is selected explicitly in `src/main/resources/application.yml`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        model: gpt-5.6-luna
```

Do not replace the environment-variable reference with a real key in a committed file.

## Main files

- `PromptController.java` — HTTP boundary and validation
- `PromptService.java` — application-facing contract
- `SpringAiPromptService.java` — Spring AI model interaction
- `application.yml` — provider model and minimal Actuator exposure
- `SpringAiPromptServiceWireMockTests.java` — deterministic provider contract test
- `PromptApiHealthTests.java` — operational endpoint exposure test
- `test-prompt-response.json` — fixed provider response used by the test
