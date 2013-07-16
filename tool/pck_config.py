# -*- coding:utf-8 -*-

"""pack game config xls to cs and java
   author: zhangshiliang
"""

import os
import sys
import xlrd
import struct
import re

import except_handler

from mako.template import Template
from mako.lookup import TemplateLookup

BYTE_ORDER = '<'
ARR_SPLIT = ','
g_CfgData_List = []

class ParseException(Exception):
    def __init__(self, id, name, native_except):
        self.id = id
        self.name = name
        self.native_except = native_except

    def __str__(self):
        return "id: " + str(self.id) + ", name: " + str(self.name)

class CfgData:
    def __init__(self, name_list, type_list, cl_list, arr_list, value_list):
        self.name_list = name_list
        self.type_list = type_list
        self.cl_list = cl_list
        self.arr_list = arr_list
        self.value_list = value_list

def parse_xls(file_name):   
    book = xlrd.open_workbook(file_name)
    if book.nsheets <= 0:
        return

    nl, tl, cl, al = __init_prop(book)
    dl = __init_data(book)

    value_list = []
    for data in dl:
        val = __parse_data(data, nl, tl, cl, al)
        value_list.append(val)
        
    return CfgData(nl, tl, cl, al, value_list)

def exp_code_file(file_name, template, cfg_data, cls_name):
    dir, name = os.path.split(file_name)
    if (dir) and (not os.path.exists(dir)):
        os.makedirs(dir)

    templ = Template(filename = template)
    txt = templ.render(input_encoding = 'utf-8',
                       output_encoding = 'utf-8',
                       type_list = cfg_data.type_list, 
                       name_list = cfg_data.name_list, 
                       client_list = cfg_data.cl_list, 
                       arr_list = cfg_data.arr_list,
                       value_list = cfg_data.value_list,
                       class_name = cls_name).encode('utf-8', 'replace')

    #txt = txt.replace('\n', '')
    file = open(file_name, 'w')
    file.write(txt)

    file.flush()
    file.close()

def __init_prop(book):
    sheet = book.sheet_by_index(0)

    #row 0 is comment
    name_list = [str(t.value).strip() for t in sheet.row(1)]
    type_list = [str(t.value).strip().lower() for t in sheet.row(2)]
    client_list = [str(t.value).strip().lower() for t in sheet.row(3)]
    arr_list = [t.endswith('[]') for t in type_list]

    return name_list, type_list, client_list, arr_list

def __init_data(book):
    data_list = []
    for sheet in book.sheets():
        if sheet.name.startswith('~'):
            continue

        for rx in range(4, sheet.nrows):
            item_list = [data.value for data in sheet.row(rx)]
            for i in range(len(item_list)):
                if type(item_list[i]) is unicode:
                    item_list[i] = item_list[i].encode('utf-8')

            if str(item_list[0]).startswith('#'):
                continue

            data_list.append(item_list)

    return data_list

def __col_cnt(cfg_data):
    cnt = 0
    for item in cfg_data.cl_list:
        if item.count('s') > 0:
            cnt += 1
    return cnt

def __convert_item(tp, value):
    str_val = str(value).lower().strip()
    if tp.lower().startswith('bool'):
        if str_val == 'false' or str_val == '0' or not str_val:
            return 0
        elif str_val == 'true' or str_val == '1':
            return 1
        else:
            raise Exception, "invalid bool data: " + str_val
    elif tp.startswith('byte'):
        if not str_val:
            value = 0
        return int(value)
    elif tp.lower().startswith('short'):
        if not str_val:
            value = 0
        return int(value)
    elif tp.lower().startswith('int'):
        if not str_val:
            value = 0
        return int(value)
    elif tp.lower().startswith('long'):
        if not str_val:
            value = 0
        return long(value)
    elif tp.lower().startswith('float'):
        if not str_val:
            value = 0
        return float(value)
    elif tp.lower().startswith('double'):
        if not str_val:
            value = 0
        return float(value)
    elif tp.lower().startswith('string'):
        #return str(value)
        return str(value).decode('utf-8')
    else:
        raise Exception, "unknown type to convert: " + tp

def __parse_data(elem_list, name_list, type_list, cl_list, arr_list):
    value_list = []
    for i in range(len(cl_list)):
        if cl_list[i].count('s') <= 0:
            value_list.append(elem_list[i])
            continue

        item = elem_list[i]
        if arr_list[i]:
            arr = []
            if not str(item).strip():
                pass # do nothing
            if type(item) is not str:
                value = __convert_item(type_list[i], item)
                arr.append(value)
            else:
                for elem in item.split(','):
                    if not elem.strip():
                        continue
                    value = __convert_item(type_list[i], 
                                           elem.strip())
                    arr.append(value)
            value_list.append(arr)
        else:
            value = __convert_item(type_list[i], item)
            value_list.append(value)

    return value_list
   
def pack_config_dir(mako_file, src_dir, code_dir):
    sys.excepthook = except_handler._excepthook
    
    if not os.path.exists(src_dir):
        return
    if not os.path.exists(mako_file):
        return
    
    if not os.path.exists(code_dir):
        os.makedirs(code_dir)

    for file in os.listdir(src_dir):
        if os.path.isfile(os.path.join(src_dir, file)) \
                and os.path.splitext(file)[1] == '.xls':
            print "pack file: " + file
            file_name = os.path.splitext(file)[0]
            code_file = file_name + '.java'
            class_name = file_name

            cfg_data = parse_xls(os.path.join(src_dir, file))
            exp_code_file(os.path.join(code_dir, code_file),
                          mako_file, cfg_data, class_name)
