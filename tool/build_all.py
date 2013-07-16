#!/usr/bin/env python

import build_conf
import build_message
import build_proto
import build_jar

def build():
    print "build game conf..."
    build_conf.build()
    print "build message..."
    build_message.build()
    print "buid proto..."
    build_proto.build()
    print "build jar..."
    build_jar.build()

if __name__ == '__main__':
    build()
