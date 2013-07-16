#!/usr/bin/env python

import os
import os.path

SRC_DIR = "../proto"
#DST_DIR = "../exp/proto"
DST_DIR = "../aquarius-base/src/main/java"

def build():
    for file in os.listdir(SRC_DIR):
        f, e = os.path.splitext(file)
        if e == ".proto":
            os.system("protoc" + " -I=" + SRC_DIR
                      + " --java_out=" + DST_DIR + " " + 
                      os.path.join(SRC_DIR, file))

if __name__ == '__main__':
    build()
