$ErrorActionPreference = "Stop"
ollama --version
ollama pull qwen3.5:9b
ollama pull qwen2.5-coder:3b
ollama pull qwen2.5-coder:1.5b
ollama create scalecanvas-qwen -f .\Modelfile.qwen35-9b
ollama list
Write-Host "Configure Continue or Roo with http://localhost:11434"
