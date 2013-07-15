package com.leonc.zodiac.aquarius.base.rpc;

/** Every method in subclass will be the handler, 
 *  which must have 1 and only 1 argument typed Command.
 *
 *  Every method will be called in one thread.
 * 
 *  class ServiceImpl implements Service {
 *      public void HandleCmd0(Command cmd) {}
 *      public void HandleCmd1(Command cmd) {}
 * }
 */
public interface Service {}
