#ifndef Example_PacketHandler_h
#define Example_PacketHandler_h

class Packet;
class PacketSock;

class PacketHandler
{
public:
    virtual ~PacketHandler() {}

    virtual void handle_packet(PacketSock* sock, const Packet& pck) = 0;
    virtual void handle_error(PacketSock* sock, int errcode) = 0;
};

#endif
