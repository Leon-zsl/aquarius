#!/usr/bin/env python

import os
import os.path

DST_DIR = "../aquarius-base/src/main/java"
SRC_DIR = "./"

for file in os.listdir(SRC_DIR):
    f, e = os.path.splitext(file)
    if e == ".proto":
        os.system("protoc --java_out=" + DST_DIR + " " + os.path.join(SRC_DIR, file))
