#!/bin/sh
# A script to set the ProKey from the user's input in the $HOME/.vaadin/proKey file easily. 

# Define the .vaadin directory path in the user's home directory
VAADIN_DIR="$HOME/.vaadin"

# Create the .vaadin directory if it doesn't exist
mkdir -p "$VAADIN_DIR"

# Define the proKey file path
PROKEY_FILE="$VAADIN_DIR/proKey"

# Check if the proKey file exists and warn the user
if [ -f "$PROKEY_FILE" ]; then
  echo "Warning: The file 'proKey' already exists and will be overwritten."
fi

# Prompt the user for their proKey
echo "Enter your proKey as given in a proKey file (for instance less ~/.vaadin/proKey ) Ctrl+c to cancel:"
read proKeyInput

# Write the input to the proKey file (this will create or overwrite the file)
echo "$proKeyInput" > "$PROKEY_FILE"

echo "The proKey file has been updated."