module CSCD454GameLayout.main {
    requires java.desktop;  // Required for java.awt and javax.swing
    requires org.slf4j;

    exports csci.ooad.layout.intf;
    exports csci.ooad.layout.example;
}