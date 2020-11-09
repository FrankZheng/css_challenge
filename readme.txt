1. How to run and test your code in a local environment?
I used java/IDEA to develop this program, used maven to mange the dependencies. So please make sure have java(>=1.8) and
maven(>=3.6.0) installed on your local machine.
To run unit tests for the code, please run "mvn test".
To run the code, please run "./run.sh"

2. A description of how and why you chose to handle moving orders to and from the overflow shelf.
When order received, we need do some processing to place order to temperature matched shelf or overflow shelf, please check
the "Order_received_process_diagram.png" for details.

3. Any other design choices you would like the interviewers to know.
Basically I split this system into three classes:
OrderIngestionWorker:
This class is used to load and parse the orders.json file and feed the order into the system. Here use an observer pattern
to decouple the order's processors and this class. Anyone can implements the OrderListener and register to this class to get
notified when order received or all orders have been processed. This class also allows to pass a constructor parameter to
control the order ingestion rate.

CourierDispatcher:
This class is used to dispatch a courier when order received and notify others when courier arrived. This class implements the
OrderListener to get notified when order received, then dispatch a courier to pick up the order sometime(2s-6s) later. Here
will run a timer to constantly check if there are couriers arrived and notify the system. Here also use an observer pattern.
Anyone can implements the CourierListener and register to this class to get notified when courier arrived.

Kitchen:
This class implements the OrderListener to get notified when order received, then serve the order and determine which shelf to
place the order. This class also implements the CourierListener to get notified when courier arrived and let courier to pick the order
from the shelf.








