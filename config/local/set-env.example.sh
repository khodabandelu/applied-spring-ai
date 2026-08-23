#!/usr/bin/env bash

# Safe template for local development. This file is committed to Git.
# Copy it to set-env.sh, replace the placeholder, and keep the copy private.
# IntelliJ IDEA can also use set-env.sh as an environment script.

export OPENAI_API_KEY="replace-with-your-openai-key-7d3f92ab"

if [ -z "${OPENAI_API_KEY:-}" ] || [ "$OPENAI_API_KEY" = "replace-with-your-openai-key-7d3f92ab" ]; then
    echo "Replace the placeholder in config/local/set-env.sh with your OpenAI API key." >&2
    return 1 2>/dev/null || exit 1
fi

echo "Local environment variables are loaded for this process."
