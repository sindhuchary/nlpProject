#!/bin/sh

#DATA=miniTest
DATA=smallData/Electronics_10kLines.aa
#DATA=genia

#PARSER=usna.parser.BaselineParser
#PARSER=usna.parser.CKYParser

java -mx500m -cp classes:lib/stanford-postagger.jar Tagger $@

#    usna.parser.ParserTester \
#    -path /courses/nchamber/nlp/lab6/data \
#    -parser $PARSER \
#    -data $DATA \
#    $@
#	java -cp ".:stanford-postagger-2015-01-30/stanford-postagger.jar" src/Tagger [inputfile]


