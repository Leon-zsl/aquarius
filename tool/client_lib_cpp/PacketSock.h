#ifndef __Example__PacketSock__
#define __Example__PacketSock__

#include <queue>
#include <string>
#include <pthread.h>

#include "Packet.h"

class PacketHandler;
class SockMgr;

class PacketSock
{
    friend class SockMgr;
public:
    void set_handler(PacketHandler* h) { _handler = h; }
    PacketHandler* get_handler() { return _handler; }
    
    std::string get_ip() const { return _ip; }
    short get_port() const { return _port; }
    
    void connect(const std::string& ip, short port);
    void disconnect();
 
    void send_packet(const Packet& pck);
    void dispatch_packet();
    
    void _recv_data();
    void _send_data();
    void _recv_error(int error) { _error = error; }
    int _get_fd() const { return _sock; }
    
private:
    PacketSock(SockMgr* mgr);
    ~PacketSock();
    
    PacketSock(const PacketSock& sock);
    void operator=(const PacketSock& sock);

private:
    typedef std::queue<Packet> PckQueue;

    SockMgr* _mgr;
    PacketHandler* _handler;
    
    std::string _ip;
    short _port;
    int _sock;
    
    volatile bool _running;
    volatile int _error;
    
    PckQueue _send_queue;
    pthread_mutex_t _send_lock;

    PckQueue _recv_queue;
    pthread_mutex_t _recv_lock;

    int _read_buf_len;
    int _read_buf_pos;
    char* _read_buf;
    
    int _write_buf_len;
    int _write_buf_pos;
    char* _write_buf;
};
#endif /* defined(__Example__PacketSock__) */
