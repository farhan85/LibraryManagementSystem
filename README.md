# Library Management System

A small project I'm writing in my spare time to demonstrate some coding design patterns I've learned over the years.
This application is a simple library management system that (eventually) can be used to keep a record of all the books
in a library, the current stock and which books have been borrowed.

## Running instructions

This application can be run using an in-memory DB (where all resources are stored in a hashmap) or using AWS DynamoDB
as the backend data store.

### In-memory datastore
```text
java -cp <classpath> org.example.library.MainApp -memdb
```

### DynamoDB datastore
```text
# Create the AWS infrastructure
aws cloudformation create-stack --stack-name <name> --template-body "file://path/to/template.yaml"

# Run application
java -cp <classpath> org.example.library.MainApp -ddb -region <AWS Region name> -profile <AWS profile name>
```

The AWS profile name comes from the credentials to use from your `~/.aws/credentials` file.