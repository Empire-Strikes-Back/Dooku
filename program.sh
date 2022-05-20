#!/bin/bash

repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:Ripley Ripley.core/process \
    :main-ns Dooku.main
}


main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m Dooku.main
}

tag(){
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  TAG="$COMMIT_COUNT-$COMMIT_HASH"
  git tag $TAG $COMMIT_HASH
  echo $COMMIT_HASH
  echo $TAG
}

jar(){

  rm -rf out/*.jar
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  clojure \
    -X:Genie Genie.core/process \
    :main-ns Dooku.main \
    :filename "\"out/Dooku-$COMMIT_COUNT-$COMMIT_HASH.jar\"" \
    :paths '["src" "out/ui" "out/corn" "out/data"]'
}

Madison_install(){
  npm i --no-package-lock
  mkdir -p out/ui/
  cp src/Dooku/index.html out/ui/index.html
  cp src/Dooku/style.css out/ui/style.css
  mkdir -p out/corn/
  cp package.json out/corn/package.json
}

Moana(){
  clj -A:Moana:ui:corn -M -m shadow.cljs.devtools.cli "$@"
}

Madison_repl(){
  Madison_install
  Moana clj-repl
  # (shadow/watch :main)
  # (shadow/watch :ui)
  # (shadow/repl :main)
  # :repl/quit
}

release(){
  rm -rf out
  Madison_install
  Moana release :ui :corn
  jar
}

"$@"