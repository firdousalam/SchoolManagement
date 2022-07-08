# SchoolManagement
School management application

## step to Install Postgres in your local machine ##

## step 1 ##
download Postgres : https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

## Step 2 ##
Use following steps

https://www.enterprisedb.com/docs/supported-open-source/postgresql/installer/02_installing_postgresql_with_the_graphical_installation_wizard/01_invoking_the_graphical_installer/

## step 3 ##

Open Postgres
 
click on server -> register -> server

## step 4 ##
In Name give as name like postgres
Under Connection give hostname : 127.0.0.1
password : admin123
tick save password

## step 5 ##

Once your server get created
create database as "klamp"
then schemas as "klamp"




## Step to run backend code in STS ##

## step 1 ##

Install and set Java Path

java 16.0.1 2021-04-20
Java(TM) SE Runtime Environment (build 16.0.1+9-24)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.1+9-24, mixed mode, sharing)

## step 2 ##

Download STS prefer 4.12



## Step 3 ##
Download Lombok from https://projectlombok.org/download

## step 4 ##
Open Lombok jar file by double click

## Step 5 ##

clcik on manual path

## step 6 ##
give SpringSuiteTool.exe path in lombok 

## step 7 ##
Open STS -> click on SpringSuiteTool.exe 
give any space name

click on Import - import as general project
Import the given service project

## step 8 ##
once you import your project wait for few minutes till all dependency get downloaded

## step 9 ##

Right click -> Maven -> Update project

Right click -> Run as -> Maven Install

Right click -> Run as -> Maven Build

Right click -> Run as -> SpringBoot Application

## Step 10 ##

add data from swagger URL

http://localhost:10010/swagger-ui.html



