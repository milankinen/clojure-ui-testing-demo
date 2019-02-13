import "todomvc-common";
import TodoStore from "./stores/TodoStore";
import ViewStore from "./stores/ViewStore";
import TodoApp from "./components/todoApp.js";
import React from "react";
import ReactDOM from "react-dom";

fetch("/api/todos")
  .then(res => res.json())
  .then(todos => {
    var todoStore = TodoStore.fromJS(todos || []);
    var viewStore = new ViewStore();

    todoStore.subscribeServerToStore();

    ReactDOM.render(
      <TodoApp todoStore={todoStore} viewStore={viewStore} />,
      document.getElementById("todoapp")
    );
  });

if (process.env.NODE_ENV !== "production") {
  if (module.hot) {
    module.hot.accept("./components/todoApp", () => {
      var NewTodoApp = require("./components/todoApp").default;
      ReactDOM.render(
        <NewTodoApp todoStore={todoStore} viewStore={viewStore} />,
        document.getElementById("todoapp")
      );
    });
  }
}
