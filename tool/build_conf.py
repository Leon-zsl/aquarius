#!/usr/bin/env python

import os
import os.path
import sys
import path_util
import pck_config

TMPL_FILE = os.path.abspath(os.path.normpath("./mako/Config_java.mako"))
CONF_DIR = os.path.abspath(os.path.normpath("../gameconf"))
CODE_DIR = os.path.abspath(os.path.normpath("../aquarius-base/src/main/java/com/leonc/zodiac/aquarius/base/conf"))
#CODE_DIR = os.path.abspath(os.path.normpath("../exp/gameconf"))

def build():
    path_util.clear_folder_type(CODE_DIR, "java")
    pck_config.pack_config_dir(TMPL_FILE, CONF_DIR, CODE_DIR)
        
if __name__ == '__main__':
    build()
