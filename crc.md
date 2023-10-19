### Class: Item

| Item ||
| :--------: | :--------: |
| Store and manage information about an inventory item   | FirestoreDatabase |
| Provide methods for retrieving and updating item information   | BarcodeScanner |
|   | SerialNumberReader |

### Class: MainMenuActivity

| MainMenuActivity ||
| :--------: | :--------: |
| Display the main menu screen of the app   | InputScreen |
| Provide options for navigating to different sections of the app   | DisplayScreen |
|   | SummaryScreen |

### Class: InputActivity

| InputActivity ||
| :--------: | :--------: |
| Allow users to input data for a new inventory item   | InventoryItem |
| Validate and store the data collected   | Camera |
| Interface with the Camera for photo capture   | BarcodeScanner |
|   | SerialNumberReader |
|   | FirestoreDatabase |

### Class: DisplayActivity

| DisplayScreen ||
| :--------: | :--------: |
| Display details of a selected inventory item   | InventoryItem |
| Allow users to view and potentially edit the item's information   | FirestoreDatabase |

### Class: SummaryActivity

| SummaryScreen ||
| :--------: | :--------: |
| Provide an overview of the inventory data, possibly in the form of statistics or charts   | InventoryItem |
| Allow users to perform summary-related actions   | FirestoreDatabase |

### Class: FirestoreDatabase

| FirestoreDatabase ||
| :--------: | :--------: |
| Connect to the Firestore database   | InventoryItem |

### Class: Camera

| Camera ||
| :--------: | :--------: |
| Capture photos of products for barcode detection and serial number reading   | InputScreen |


### Class: BarcodeScanner

| BarcodeScanner ||
| :--------: | :--------: |
| Identify and decode barcodes from photos   | InputScreen |

### Class: SerialNumberReader

| SerialNumberReader ||
| :--------: | :--------: |
| Detect and read serial numbers from photos   | InputScreen |

### Class: ItemAdapter

| SerialNumberReader ||
| :--------: | :--------: |
| Connect the list of items to the ListView   | InputScreen |
