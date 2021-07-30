# Introduction
This project is a proof of concept data analysis for an online gift retailer situated in the UK, known as London Gift Shop (LGS). The purpose of the analysis was to better understand customer shopping behaviours, and improve the rapport between customers and the marketing department such that promotions and other forms of communication can better serve to increase the companys revenue. Data analytics were performed on the provided csv data file using Python Pandas. Preliminary analyses were performed with the help of a locally loaded data warehouse (PostgreSQL database), in order to familiarize myself with the characteristics of the data. The main analysis was done in a Python Jupyter notebook, contained in this GitHub repository for reference.

 # Implementation
 ## Project Architecture
LGS has its own data warehouse, collecting all sorts of marketing data such as customer invoices. Their front end consists of a browser application, and data is initially dumped into an Azure Blob storage. Their back end consists of an AKS cluster, and an OLTP system (Azure SQL Server) for storing the processed data. Their data warehouse provided Jarvis with a SQL file containing transaction data between 01/12/2009 and 09/12/2011, and that was used for analytics in this project.

 - Draw an architecture Diagram (please do not copy-paste any diagram from the project board) 

## Data Analytics and Wrangling
 - Create a link that points to your Jupyter notebook (use the relative path `./retail_data_analytics_wrangling.ipynb`)
 - Discuss how would you use the data to help LGS to increase their revenue (e.g. design a new marketing strategy with data you provided)

 # Improvements - List three improvements that you want to do if you got more time
