## -*- coding: utf-8 -*-
<%def name="conv_type(type)"><%
func = ''
if type.startswith("bool"):
    func = type.replace("boolean", "bool").replace("bool", "boolean")
elif type.startswith("string"):
    func = type.replace("string", "String")
else:
	func = type
return func
%></%def>
<%def name="conv_value(type, value)"><%
val = ''
if type.startswith("bool"):
   if value == 0 or value == "0":
   	  val = "false"
   elif value == 1 or value == "1":
   	  val = "true"
   else:
      val = str(value)
elif type.startswith("float"):
   val = "" + str(value) + "f"
elif type.startswith("short"):
   val = "(short)" + str(value);
elif type.startswith("string"):
   val = u"\"" + value + u"\""
else:
   val = "" + str(value)
return val
%></%def>
<%def name="type_of_arr(arr)"><%
type = arr.strip('[]')
if type.startswith("bool"):
    func = type.replace("boolean", "bool").replace("bool", "boolean")
elif type.startswith("string"):
    func = type.replace("string", "String")
else:
	func = type
return func
%></%def>
<%
last_item_index = 0
for i in range(len(client_list)):
    if client_list[i].count('s') > 0:
        last_item_index = i + 1
%>
<%
item_count = 0
for i in range(len(client_list)):
    if client_list[i].count('s') > 0:
        item_count += 1
%>
<%
has_array_item = False
for i in range(len(client_list)):
    if client_list[i].count('s') > 0:
        if arr_list[i]:
            has_array_item = True
            break
%>
package com.leonc.zodiac.aquarius.base.conf;

import java.util.HashMap;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ${class_name}
{
    public static HashMap<Integer, ${class_name}> dic = new HashMap<Integer, ${class_name}>();
    public static ArrayList<${class_name}> array = new ArrayList<${class_name}>();

	private static Log logger = LogFactory.getLog(${class_name}.class);

% for i in range(len(client_list)):
    % if client_list[i].count('s') > 0:
    public final ${conv_type(type_list[i])} ${name_list[i]};
    % endif
% endfor
    
    public ${class_name}(\
% for i in range(last_item_index - 1):
    % if client_list[i].count('s') > 0:
${conv_type(type_list[i])} ${name_list[i]}, \
    % endif
% endfor
${conv_type(type_list[last_item_index - 1])} ${name_list[last_item_index - 1]}\
)
    {
% for i in range(last_item_index):
   % if client_list[i].count('s') > 0:
        this.${name_list[i]} = ${name_list[i]};
   % endif
% endfor
    }

    static {
% for i in range(len(value_list)):
  	   addItem${i}();
% endfor
    }

% for i in range(len(value_list)):
    private static void addItem${i}()
    {
% if has_array_item:
        int arr_item_len_${class_name};
% endif
% for j in range(last_item_index):
    % if client_list[j].count('s') > 0:
        % if arr_list[j]:
        arr_item_len_${class_name} = ${len(value_list[i][j])};
        ${conv_type(type_list[j])} ${name_list[j]} = new ${type_of_arr(type_list[j])} [arr_item_len_${class_name}];
	        % for k in range(len(value_list[i][j])):
        ${name_list[j]}[${k}] = ${conv_value(type_list[j],value_list[i][j][k])};
			% endfor		
        % else:
        ${conv_type(type_list[j])} ${name_list[j]} = ${conv_value(type_list[j], value_list[i][j])};
        % endif
    % endif
% endfor
        ${class_name} new_obj_${class_name} = new ${class_name}(\
% for j in range(last_item_index - 1):
     % if client_list[j].count('s') > 0:
${name_list[j]}, \
     % endif
% endfor
${name_list[last_item_index - 1]}\
);
        if(${name_list[0]} == 0) {
            logWarning("invalid key: " + ${name_list[0]});
            return;
        }
        if(dic.containsKey(new Integer(${name_list[0]}))) {
            logWarning("duplicate key: " + ${name_list[0]});
            return;
        }

        dic.put(new Integer(${name_list[0]}), new_obj_${class_name});
        array.add(new_obj_${class_name});
    }
% endfor

	private static void logWarning(String msg) {
	    logger.warn(msg);
    }
}
