#!/bin/bash

# navigate to the repository directory
cd pwd

# list all Git branches and format the output
branches=$(git branch -a | grep -v HEAD | sed 's/remotes\/origin\///g' | sed 's/^[ *]*//' | sed 's#\(.*\)#\1#' | tr '\n' ' ')

# print out the branches in a format that can be used by the git checkout command
echo "The available branches are: "
echo "$branches"

# exit the script
exit 0
