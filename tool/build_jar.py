#!/usr/bin/env python

import sys
import os
import path_util

def build_base():
    os.chdir("../aquarius-base")
    os.system("mvn clean install")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_master():
    os.chdir("../aquarius-master")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_gate():
    os.chdir("../aquarius-gate")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_db():
    os.chdir("../aquarius-db")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_world():
    os.chdir("../aquarius-world")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_stage():
    os.chdir("../aquarius-stage")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_chat():
    os.chdir("../aquarius-chat")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build_account():
    os.chdir("../aquarius-account")
    os.system("mvn clean package")

    srcdir = os.path.abspath("target")
    dstdir = os.path.abspath("../release")
    if not os.path.exists(dstdir):
        os.makedirs(dstdir)
    path_util.copy_folder_type(srcdir, dstdir, ".jar")

def build():
    build_base()
    build_master()
    build_gate()
    build_db()
    build_world()
    build_stage()
    build_chat()
    build_account()

if __name__ == '__main__':
    for arg in sys.argv:
        if arg == 'base' or arg == 'all':
            build_base()
            break
    for arg in sys.argv:
        if arg == 'master' or arg == 'all':
            build_master()
            break
    for arg in sys.argv:
        if arg == 'gate' or arg == 'all':
            build_gate()
            break
    for arg in sys.argv:
        if arg == 'db' or arg == 'all':
            build_db()
            break
    for arg in sys.argv:
        if arg == 'world' or arg == 'all':
            build_world()
            break
    for arg in sys.argv:
        if arg == 'stage' or arg == 'all':
            build_stage()
            break
    for arg in sys.argv:
        if arg == 'chat' or arg == 'all':
            build_chat()
            break
    for arg in sys.argv:
        if arg == 'account' or arg == 'all':
            build_account()
            break
