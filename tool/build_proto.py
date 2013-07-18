#!/usr/bin/env python

import os
import os.path
import path_util

SRC_DIR = "../proto"
INC_DIR = "."
#DST_DIR = "../exp/proto"
DST_DIR = "../aquarius-base/src/main/java"

def build():
    path_util.clear_folder_type(os.path.abspath(DST_DIR), ".java")

    pwd = os.getcwd()
    os.chdir(SRC_DIR)
    for file in os.listdir("./"):
        f, e = os.path.splitext(file)
        if e == ".proto":
            os.system("protoc" + " -I=" + INC_DIR
                      + " --java_out=" + DST_DIR + " " + file)
    os.chdir(pwd)

if __name__ == '__main__':
    build()
