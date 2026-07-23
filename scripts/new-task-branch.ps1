param([Parameter(Mandatory=$true)][string]$TaskName)
$slug = $TaskName.ToLower() -replace '[^a-z0-9]+','-' -replace '^-|-$',''
git switch main
git pull
git switch -c "feature/$slug"
git status
