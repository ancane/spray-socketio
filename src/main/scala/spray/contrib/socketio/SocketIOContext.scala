package spray.contrib.socketio

import akka.actor.ActorRef
import spray.contrib.socketio.SocketIOConnection.SendEvent
import spray.contrib.socketio.SocketIOConnection.SendJson
import spray.contrib.socketio.SocketIOConnection.SendMessage
import spray.contrib.socketio.SocketIOConnection.SendPacket
import spray.contrib.socketio.packet.Packet
import spray.contrib.socketio.transport.Transport
import spray.json.JsValue

/**
 * Socket.IO has built-in support for multiple channels of communication
 * (which we call "multiple sockets"). Each socket is identified by an endpoint
 * (can be omitted).
 *
 * During connecting handshake (1::), endpoint is "", the default endpoint.
 * The client may then send ConnectPacket with endpoint (1::/endp1) and
 * (1::/endp2) etc to use the same sender-context pair as multiple sockets.
 * @See Namespace
 *
 * @Note let this context not to be final, so business application can store more
 * states in it.
 */
class SocketIOContext(val transport: Transport, val sessionId: String, val transportActor: ActorRef) {

  private var _conn: ActorRef = _
  def conn = _conn
  def withConnection(conn: ActorRef) {
    _conn = conn
  }

  def sendMessage(msg: String)(implicit endpoint: String) {
    conn ! SendMessage(msg)
  }

  def sendJson(json: JsValue)(implicit endpoint: String) {
    conn ! SendJson(json)
  }

  def sendEvent(name: String, args: List[JsValue])(implicit endpoint: String) {
    conn ! SendEvent(name, args)
  }

  def send(packet: Packet) {
    conn ! SendPacket(packet)
  }

  def onDisconnect() {
    //namespace.onDisconnect(this)
    //clientActor.removeChildClient(this);
  }

  //  def disconnect() {
  //    send(DisconnectPacket(namespace))
  //    onDisconnect()
  //  }
  //

}
