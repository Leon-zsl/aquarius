#ifndef __Example__SockMgr__
#define __Example__SockMgr__

#include <map>
#include <pthread.h>
#include <sys/socket.h>

class PacketSock;
class SockMgr
{
public:
    SockMgr();
    ~SockMgr();
    
    void start();
    
    PacketSock* create_sock();
    void destroy_sock(PacketSock* sock);
    
    void _add_sock(PacketSock* sock);
    void _del_sock(PacketSock* sock);
    
private:
    static void* select_loop(void* sock);
    
private:
    typedef std::map<int, PacketSock*> SockMap;
    
    volatile bool _running;
    
    volatile int _maxfd;
    
    pthread_mutex_t _lock;
    SockMap _sock_map;
    
    pthread_t _select_thread;
};

#endif /* defined(__Example__SockMgr__) */
