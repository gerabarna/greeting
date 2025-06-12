#!/usr/bin/bash

echo "starting ollama serve..."
ollama serve &
PID=$!
echo "ollama serve started with PID=$PID"

echo "pulling llama3 model..."
if ! ollama list | grep -q "llama3.2:3b"; then
    echo "📥 Pulling llama3.2:3b model..."
    ollama pull llama3.2:3b
    echo "⏳ Waiting for model to be ready..."
    sleep 10
fi

ollama run llama3.2:3b
wait $PID