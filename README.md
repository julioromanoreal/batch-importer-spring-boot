## Batch Importer - Spring Boot

Program to read .dat files from a specific directory, analyze the files and produce a report with specific information.

-----

<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#future-improvements">Future improvements</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

---

## About The Project

This is a simple project containing an endpoint that triggers a process that reads .dat files from a specific directory, analyze them and produce a report with specific information.

This is an example of a file to be parsed:

```aidl
001ç1234567891234çPedroç50000
001ç3245678865434çPauloç40000.99
002ç2345675434544345çJose da SilvaçRural
002ç2345675433444345çEduardo PereiraçRural
003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
```

The first part of each line defines the type of data and each type has a different layout. These are the available options:

* `001`: Data from salesmans as in `001çCPFçNameçSalary`
* `002`: Data from customers as in `002çCNPJçNameçBusiness Area`
* `003`: Data from sales as in `003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name`

After processing all files within the specified directory, the system must create a file within the output directory containing the following information:

* Quantity of customers in the files
* Quantity of salesman in the files
* ID of the most expensive sale
* The worst salesman

The name of the output file should end with `.done.dat`

### Built With

* Java 15
* [Spring Boot](https://github.com/spring-projects/spring-boot)
* [Docker](https://github.com/docker)
* [Maven](https://github.com/apache/maven)

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

Make sure the following prerequisites are installed in your machine:

* Java 15
  
Run the following command to confirm:

```sh-session
$ java -version
openjdk version "15.0.1" 2020-10-20
OpenJDK Runtime Environment (build 15.0.1+9-18)
OpenJDK 64-Bit Server VM (build 15.0.1+9-18, mixed mode, sharing)
```

* Maven


  Run the following command to confirm:

```sh-session
$ mvn --version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
```

* Docker


  Run the following command to confirm:

```sh-session
$ docker --version
Docker version 20.10.0, build 7287ab3
```

### Installation

1. Clone the repo
```sh
$ git clone https://github.com/julioromanoreal/batch-importer-spring-boot.git
```
2. Install Maven dependencies
```sh
$ mvn package
```
3. Optionally a Docker image may be created and included in `docker-compose.yaml`
```sh
$ mvn spring-boot:build-image
```
4. Start Docker containers
```sh
$ docker-compose up
```
5. Set the required properties at `src/main/resources/application.yaml`
    * `application.sales-data-in-dir`: Directory in which the .dat files will be
    * `application.sales-data-out-dir`: Directory in which the output file will be created


6. Start the application
```sh
$ ./mvnw spring-boot:run
```

## Usage

An endpoint will be available at `http://localhost:8080/batch-processing/SALES` accepting POST requests that may be sent via `curl`
```sh
$ curl -X POST http://localhost:8080/batch-processing/SALES
```
The server will read all .dat files in the directory specified as the `application.sales-data-in-dir`, analyze them and produce a report in the directory specified as the `application.sales-data-out-dir` 

## Future improvements
* Consider the usage of [Spring Batch](https://spring.io/projects/spring-batch) or [Apache Spark](https://spark.apache.org/) to process and analyze the files
* Consider working with [Apache Hadoop](https://hadoop.apache.org/) to better deal with a larger set of data

## Contributing

Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Julio Romano - [@julioromano_](https://twitter.com/julioromano_) - julio.romano@gmail.com

Project Link: [https://github.com/julioromanoreal/batch-importer-spring-boot](https://github.com/julioromanoreal/batch-importer-spring-boot)
