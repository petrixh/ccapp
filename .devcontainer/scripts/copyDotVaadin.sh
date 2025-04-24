#!/bin/sh

# A script to copy the proKey file from the /dot-vaadin directory inside the dev container to the $HOME/.vaadin directory inside the dev container, 
# i.e. use the same proKey in the dev container as on the host.

# Check if the source file exists
if [ -f "/dot-vaadin/proKey" ]; then
  # Ensure the destination directory exists
  mkdir -p "$HOME/.vaadin"
  # Copy the file to the destination
  cp "/dot-vaadin/proKey" "$HOME/.vaadin/proKey"
  echo "proKey copied successfully to $HOME/.vaadin/"
else
  echo "File /dot-vaadin/proKey does not exist."
fi