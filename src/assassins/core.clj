(ns assassins.core)
(use 'assassins.tcpserver)
"
开发思路:
逻辑上:
  Server / Client 模式
  Server 没有具体的IP和端口. 只有一个名称(类似域名),它一个网络.
  网络负责提供服务.
  Client  负责请求服务.
物理上:
  逻辑上的Server是由多个实际Node组合而成的网络.
  网络是动态的.
  网络的实际服务质量取决于Client发现的网络质量.
"
(defn -main [& args]
  (println "good job"))

(defn LogicClient "I'm a logic client" [] "")

(defn LogicServer "I'm a logic Server" [] "")

(defn resouce [server]
      (server)
      {:hello "world"})
(defn request [param-fn]
    (param-fn (resouce LogicServer)))

(defn handler [reader writer]
  (println (.readLine reader))
  (.append writer "Hello World")
  (.flush writer))


  


(defn build-net "先自建一个网络" []
  (let [server (tcp-server 
                 :port    5000
                 :host "0.0.0.0"
                 :handler (wrap-io handler))
          _ (start server)]
      server))

(defn socket [host port]
  (let [socket (java.net.Socket. host port)
        in (java.io.BufferedReader. 
            (java.io.InputStreamReader. (.getInputStream socket)))
        out (java.io.PrintWriter. (.getOutputStream socket))]
    {:in in :out out}))

(defn say-hi 
  ([] (say-hi nil))
  ([ip]
   (let [host  (if (nil? ip)  "127.0.0.1" ip)
         conn (socket host 5000)
         out (:out conn)
         in (:in conn)
         _ (.append out "hi\n")
         _ (.flush out)
         _ (println (slurp in))]
       (.close out)
       (.close in))))
