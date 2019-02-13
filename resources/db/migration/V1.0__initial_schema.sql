
CREATE TABLE todo (
  id UUID PRIMARY KEY,
  completed BOOL NOT NULL,
  title TEXT NOT NULL
);

CREATE INDEX todo_id_idx ON todo (id);
