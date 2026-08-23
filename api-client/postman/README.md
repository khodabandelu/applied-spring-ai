# Postman API Client

This directory contains an import-ready Postman collection for the runnable applications in this repository.

## Import into Postman

1. Open Postman and select **Import**.
2. Import `applied-spring-ai.postman_collection.json`.
3. Import `local.postman_environment.json`.
4. Select **Applied Spring AI - Local** as the active environment.

The collection also defines `baseUrl` itself, so the requests work at `http://localhost:8080` even when no environment is selected.

## Postman Native Git

The repository root contains `.postman/resources.yaml` and the generated `postman/` workspace files. In the Postman desktop app, connect the complete `applied-spring-ai` repository folder and use Local View. Do not connect this `api-client/postman` subdirectory as a separate workspace.

The JSON files in this directory remain the importable local resources. Postman's Native Git representation under the repository-level `postman/` directory mirrors the connected workspace.

## Run the Prompt API

From the repository root, load the local server configuration and start the application:

```bash
source ./config/local/set-env.sh
./gradlew :examples:prompt-api:bootRun
```

Then run the requests inside the **Prompt API** and **Operations** collection folders.

| Request | Expected result |
|---|---|
| Submit a prompt | `201 Created` with a non-empty `response` string |
| Reject a blank prompt | `400 Bad Request` without calling the model provider |
| Check application health | `200 OK` with `status` equal to `UP` |

The success response saved in the collection is representative. Model output can vary, so the Postman tests verify the status, content type, and response shape instead of comparing the generated sentence exactly.

## Security

The committed Postman environment contains only the local base URL. It does not need and must never contain `OPENAI_API_KEY` because Postman calls the Spring application, not OpenAI directly.

The Spring application reads the key from `config/local/set-env.sh`, which is ignored by Git. Keep any private Postman environment in a file ending with `.private.postman_environment.json`; that pattern is also ignored by Git.
