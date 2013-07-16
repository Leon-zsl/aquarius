#!/usr/bin/env python

import build_conf
import build_message
import build_proto
import build_jar

def build():
    build_conf.build()
    build_message.build()
    build_proto.build()
    build_jar.buid()

if __name__ == '__main__':
    build()
