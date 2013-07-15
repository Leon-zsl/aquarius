package com.leonc.zodiac.aquarius.base.rpc;

/** every method in subclass will be the handler, 
 *  which argument must be Command.
 *  class ServiceImpl implements Service {
 *      public void HandleCmd0(Command cmd) {}
 *      public void HandleCmd1(Command cmd) {}
 * }
 */
public interface Service {}
