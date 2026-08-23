# Security Policy

## Reporting a vulnerability

Do not report suspected vulnerabilities, exposed credentials, or other sensitive security issues in a public GitHub issue.

Use GitHub's private vulnerability reporting feature from the repository's **Security** tab when it is available. If private reporting is not enabled, contact the repository owner privately through the contact information on their GitHub profile.

Include enough information to reproduce and assess the issue, but do not include real API keys, access tokens, personal data, or credentials in the report.

## Secrets

Provider credentials must be supplied through environment variables or a managed secret store. If a key is accidentally committed or published, revoke it immediately. Removing it from a later commit does not remove it from Git history.

### Local development

Local developers may keep exports in the ignored `config/local/set-env.sh` file. It should be readable only by its owner:

```bash
cp config/local/set-env.example.sh config/local/set-env.sh
chmod 600 config/local/set-env.sh
# Replace the placeholder in config/local/set-env.sh
source ./config/local/set-env.sh
```

Never add real values to tracked shell files, commands recorded in documentation, test fixtures, or application configuration.

### Deployed environments

Staging and production must obtain secrets from a managed secret service and inject them into the server process. Do not copy or bake `config/local/set-env.sh` into an image or deployment package.

For a future AWS deployment, use AWS Secrets Manager with workload permissions supplied by an IAM role. Do not create long-lived AWS access keys merely to retrieve application secrets.

## Supported versions

This repository is an evolving collection of examples. Security fixes are applied to the latest version on the `main` branch.
