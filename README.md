# crawling-service
## Task:
The goal of this project is to collect data from such publishing sites as [makler.md](https://makler.md/), [999.md](https://999.md/), distinguish between new/old data, find duplicates and see the price changes.
A lot of published data is presented on the site for a long time and just gets reuploaded automatically with updated timestamp so there is no way to understand if the data is really new from the main page.

Functionality:
* Get list of freshly added articles
* Get article price changes through time
* Get article history
## Examples:

### New/old announcement problem:
#### Seems like a new announcement:
![image](https://user-images.githubusercontent.com/47188222/217764152-4a353db0-8bf5-4eb2-9ead-5cc6be6d3644.png)
#### Actually really old with a lot of views:
![image](https://user-images.githubusercontent.com/47188222/217763749-06af7d11-f4ef-43c3-bb06-192808815f2b.png)
