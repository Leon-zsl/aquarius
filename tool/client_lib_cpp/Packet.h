#ifndef __Example__Packet__
#define __Example__Packet__

#include <iostream>

class Packet
{
public:
    Packet();
    Packet(const Packet& pck);
    Packet(int op, const char* data, int datalen);
    ~Packet();
    
    void operator=(const Packet& pck);
    
    inline int opcode() const { return _opcode; }
    inline int data_len() const { return _data_len; }
    inline const char* data() const { return _data; }
    
private:
    int _opcode;
    int _data_len;
    char* _data;
};

#endif /* defined(__Example__Packet__) */
