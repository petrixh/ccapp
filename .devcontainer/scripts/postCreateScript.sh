#!/bin/sh

echo '\n********\n* Running postCreateScript.sh\n********\n'

echo 'Copying .vaadin folder to users home .vaadin folder if available'
/workspace/.devcontainer/scripts/copyDotVaadin.sh 

echo "Ensuring .m2 folder exists and is writable by ubuntu user"
sudo mkdir -p .m2
sudo chown -R ubuntu:ubuntu .m2 

echo '\nIf proKey is required but was not copied above, run /workspace/scripts/setProKey.sh for easy way'
echo '********'
