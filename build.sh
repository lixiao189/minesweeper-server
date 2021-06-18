#! /bin/sh
mvn compile
cd target/classes
jar --create --file ../out.jar  --main-class team.minesweeper.service.App -v -C . .
echo "build finished"
cd ..
java -jar out.jar --test
