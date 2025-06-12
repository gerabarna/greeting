#!/bin/bash

set -eo pipefail

function create() {
    while [[ "$#" -gt 0 ]]; do
      case $1 in
        name|n)               shift;    readonly name=$1;           shift;;
        date-of-birth|dob|d)  shift;    readonly date_of_birth=$1;  shift;;
        gender|g)             shift;    readonly gender=$1;         shift;;
        interests|i)          shift;    readonly interests=$1;      shift;;
        *)              echo \
        "Unknown argument:$1
Create a person. Legal arguments are (required fields marked with '*'):
  *name|n <name>                        - the name of the person
  *date-of-birth|dob|d <date of birth>  - the date of birth for the person with yyyy-mm-dd syntax
  gender|g  <MALE|FEMALE|OTHER>         - the gender of the person. possible values: MALE|FEMALE|OTHER
  interests|i <interst1,i2,...>         - comma separated list of interests for the person
  help                                  - this lovely message.";
        exit 1;
      esac
    done

    local command="curl -X 'POST'"
    if [[ -v name ]]; then
      command="$command --data-urlencode 'name=$name'"
    fi
    if [[ -v date_of_birth ]]; then
      command="$command --data-urlencode 'birthDate=$date_of_birth'"
    fi
    if [[ -v gender ]]; then
      command="$command --data-urlencode 'gender=$gender'"
    fi
    if [[ -v interests ]]; then
      command="$command --data-urlencode 'interests=$interests'"
    fi
    command="$command $host/person/create"
    echo "executing command=$command"
    eval $command
}

function update() {
    while [[ "$#" -gt 0 ]]; do
      case $1 in
        id)                   shift;    readonly id=$1;             shift;;
        name|n)               shift;    readonly name=$1;           shift;;
        date-of-birth|dob|d)  shift;    readonly date_of_birth=$1;  shift;;
        gender|g)             shift;    readonly gender=$1;         shift;;
        interests|i)          shift;    readonly interests=$1;      shift;;
        *)              echo \
        "Unknown argument:$1
Update a person. Empty fields are left unchanged in the application. Legal arguments are (required fields marked with '*'):
  *id <id>                              - the id of the person
  name|n <name>                         - the name of the person
  date-of-birth|dob|d <date of birth>   - the date of birth for the person with yyyy-mm-dd syntax
  gender|g  <MALE|FEMALE|OTHER>         - the gender of the person. possible values: MALE|FEMALE|OTHER
  interests|i <interst1,i2,...>         - comma separated list of interests for the person. If any supplied all interests are replaced
";
        exit 1;
      esac
    done

    local command="curl -X 'PATCH'"
    if [[ -v id ]]; then
          command="$command --data-urlencode 'id=$id'"
    fi
    if [[ -v name ]]; then
      command="$command --data-urlencode 'name=$name'"
    fi
    if [[ -v date_of_birth ]]; then
      command="$command --data-urlencode 'birthDate=$date_of_birth'"
    fi
    if [[ -v gender ]]; then
      command="$command --data-urlencode 'gender=$gender'"
    fi
    if [[ -v interests ]]; then
      command="$command --data-urlencode 'interests=$interests'"
    fi
    command="$command $host/person/update"
    echo "executing command=$command"
    eval $command
}

function list() {
    command="curl -X 'GET' $host/person/list"
    echo "executing command=$command"
    eval $command
}

function delete() {
    while [[ "$#" -gt 0 ]]; do
      case $1 in
        id)                   shift;    readonly id=$1;             shift;;
        *)              echo \
        "Unknown argument:$1
Deletes a person. Legal arguments are (required fields marked with '*'):
  *id <id>                              - the id of the person
";
        exit 1;
      esac
    done

    local command="curl -X 'DELETE'"
    if [[ -v id ]]; then
          command="$command --data-urlencode 'id=$id'"
    fi
    command="$command $host/person/delete"
    echo "executing command=$command"
    eval $command
}

function greet() {
    while [[ "$#" -gt 0 ]]; do
      case $1 in
        id)                   shift;    readonly id=$1;             shift;;
        *)              echo \
        "Unknown argument:$1
Generates a birthday message for a person. Legal arguments are (required fields marked with '*'):
  *id <id>                              - the id of the person
";
        exit 1;
      esac
    done

    command="curl -X 'GET' $host/person/greet?id=$id"
    echo "executing command=$command"
    eval $command
}


function main() {
  case $1 in
    create)      shift; create "$@";;
    update)      shift; update "$@";;
    list)        shift; list   "$@";;
    delete)      shift; delete "$@";;
    greet)       shift; greet  "$@";;
    *)              echo \
"Unknown argument:$1
Simple CLI for the LLM birthday greeting application. For further help type 'help' for one of the sub-commands.
Legal sub-commands are:
  create - create a new person
  update - update an existing person
  list   - list all people in the system
  delete - delete an existing person
  greet  - generate a birthday greeting for the person
  help   - print this lovely message.";
        exit 1;
      esac
}

readonly host=http://localhost:8080
main "$@"