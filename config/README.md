# Environment configuration

Application code reads configuration from environment variables. Where those variables originate depends on the environment.

## Local development

Copy the safe committed template to the ignored local file:

```bash
cp config/local/set-env.example.sh config/local/set-env.sh
chmod 600 config/local/set-env.sh
```

Open `config/local/set-env.sh`, replace the fake placeholder, and keep all local exports in that file:

```bash
export OPENAI_API_KEY="your-local-key"
```

Protect and load it from the repository root:

```bash
source ./config/local/set-env.sh
```

The example script is safe to commit because it contains no real credentials. The copied `set-env.sh` is ignored by Git and must not be copied into a container or deployment package. Sourcing it affects only the current terminal and processes started from that terminal.

### IntelliJ IDEA

IntelliJ IDEA does not automatically inherit variables loaded in an unrelated terminal. Configure the Spring Boot run configuration to load the ignored script directly:

1. Open **Run > Edit Configurations**.
2. Select **PromptApiApplication**.
3. Find **Environment variables**. Use **Modify options** to enable the field if it is hidden.
4. Select **Browse for .env files and scripts**.
5. Choose `$PROJECT_DIR$/config/local/set-env.sh`.
6. Remove any manually entered `OPENAI_API_KEY=${OPENAI_API_KEY}` value, then apply the configuration and restart the application.

The environment script applies only to that Run/Debug launch. The secret remains in the ignored local file and is not added to the project configuration.

## Staging and production

Managed environments must inject the same variable names from their secret-management platform. For a future AWS deployment, the intended source is AWS Secrets Manager with access granted to the workload through an IAM role.
