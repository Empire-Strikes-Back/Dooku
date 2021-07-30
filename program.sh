#!/bin/bash

push(){
  ORIGIN=$(git remote get-url origin)
  rm -rf .git
  git init -b main
  git remote add origin $ORIGIN
  git config --local include.path ../.gitconfig
  git add .
  git commit -m "i am play-scenarios-in-browser program"
  git push -f -u origin main
}

"$@"