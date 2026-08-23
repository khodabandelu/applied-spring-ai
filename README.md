# Applied Spring AI

Production-minded Spring AI examples for Java developers.

This repository explores how to move from a basic model call to AI features that can be tested, evaluated, observed, and operated with confidence. Every topic includes a focused Spring Boot application, deterministic tests, and runnable API requests.

## Available examples

| Topic | What it demonstrates | Application | API client |
|---|---|---|---|
| Prompt API | `ChatClient`, external configuration, validation, health monitoring, and deterministic provider testing | [View the code](examples/prompt-api) | [Run with Postman](api-client/postman) |

## Prompt API

The first example exposes a small HTTP API that accepts a prompt and returns the model response:

```http
POST /api/prompts
Content-Type: application/json
```

```json
{
  "prompt": "Explain why idempotency matters in distributed systems in one sentence."
}
```

```json
{
  "response": "Idempotency allows repeated operations to produce the same effect, making retries safe when network failures occur."
}
```

The web layer does not know which model provider is being used. Provider access stays behind `PromptService`, while Spring AI's `ChatClient` handles the model interaction.

Spring Boot Actuator exposes a minimal operational endpoint without revealing environment values:

```http
GET /actuator/health
```

```json
{
  "groups": ["liveness", "readiness"],
  "status": "UP"
}
```

## Technology

| Component | Version |
|---|---:|
| Java | 25 |
| Spring Boot | 4.1.0 |
| Spring AI | 2.0.0 |
| Spring Boot Actuator | 4.1.0 |
| Gradle | 9.4.1 |
| WireMock | 4.0.9 |
| Default model | `gpt-5.6-luna` |

The model is selected explicitly so a framework default cannot silently change application behavior. GPT-5.6 Luna supports Chat Completions and is positioned for cost-sensitive workloads in the [official OpenAI model documentation](https://developers.openai.com/api/docs/models/gpt-5.6-luna).

## Run the project

Run every automated test from the repository root:

```bash
./gradlew test
```

The Prompt API contract test uses WireMock. It does not need an OpenAI API key and does not spend model tokens.

### Local environment

Copy the committed environment template to the ignored local file:

```bash
cp config/local/set-env.example.sh config/local/set-env.sh
chmod 600 config/local/set-env.sh
```

Open `config/local/set-env.sh` and replace the fake placeholder with your local key. Keep all local exports in this ignored file:

```bash
export OPENAI_API_KEY="your-local-key"
```

Load all exports into the current terminal, then run the application:

```bash
source ./config/local/set-env.sh
./gradlew :examples:prompt-api:bootRun
```

The complete `config/local/set-env.sh` file is ignored by Git. Sourcing affects only the current terminal session; it does not permanently modify the operating system. See [config/README.md](config/README.md) for the environment convention.

### Staging and production

Do not deploy `config/local/set-env.sh`. Staging and production must inject `OPENAI_API_KEY` from the platform's managed secret service. The application continues to read the same environment-variable name, so no Java or Spring configuration changes are required between environments.

When this project is deployed to AWS, the intended design is to keep the value in AWS Secrets Manager and grant the workload access through an IAM role instead of storing AWS credentials or the OpenAI key in the repository.

Submit a prompt from another terminal:

```bash
curl --fail-with-body http://localhost:8080/api/prompts \
  -H 'Content-Type: application/json' \
  -d '{"prompt":"Explain why idempotency matters in distributed systems in one sentence."}'
```

## Repository structure

```text
applied-spring-ai/
├── .postman/
│   └── resources.yaml       # Postman Native Git workspace mapping
├── api-client/
│   └── postman/             # Import-ready requests, examples, and checks
├── postman/                 # Postman Native Git collections and environments
├── examples/
│   └── prompt-api/          # Runnable Spring Boot application
├── config/
│   ├── local/
│   │   └── set-env.example.sh # Safe template for local variables
│   └── README.md              # Local and deployed environment convention
├── gradle/                  # Shared Gradle Wrapper
├── .github/workflows/       # Tests every example on GitHub
├── build.gradle             # Shared Java and plugin configuration
├── settings.gradle          # Multi-project module registry
├── gradlew
├── LICENSE
└── README.md
```

Examples are organized by capability so paths remain stable as the repository grows. The root Gradle build discovers each registered example and `./gradlew test` validates the complete repository.

## Planned topics

- Prompt templates and message roles
- Structured output
- Conversation memory
- Retrieval-augmented generation
- Tool calling
- Model Context Protocol
- Multimodal input and output
- Evaluation and observability

Each new capability will have its own directory under `examples/`, focused documentation, deterministic tests, and matching Postman requests when it exposes HTTP endpoints.

## Security

Never commit a real provider key. Use an environment variable locally and a managed secret store in deployed environments. If a key is accidentally committed, revoke it immediately; removing it in a later commit is not sufficient.

Read the complete [security policy](SECURITY.md) before reporting a vulnerability or exposed credential.

## Contributing

Contributions are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for repository conventions, testing commands, and the process for adding an example.

## License

This project is available under the [MIT License](LICENSE).
