import React, { useState } from 'react';

function ExampleForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = event => {
    event.preventDefault();
    console.log(`Name: ${name}, Email: ${email}`);
  };

  return (
    <div>
      <h2>Create Group</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="name">Group Name:</label>
        <input
          type="text"
          id="name"
          name="name"
          value={name}
          onChange={event => setName(event.target.value)}
          placeholder="Enter your  group's name"
        />
        <label htmlFor="email">Subject:</label>
        <input
          type="text"
          id="email"
          name="email"
          value={email}
          onChange={event => setEmail(event.target.value)}
          placeholder="Enter your group's subject"
        />
        <button type="submit">Create</button>
      </form>
    </div>
  );
}

export default ExampleForm;
