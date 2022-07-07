# Error Handling Beyond the Standard Library

Slides and sample code for the **Error Handling Beyond the Standard Library** talk.

To run it in a local environment using [reveal.js](https://github.com/hakimel/reveal.js/) execute:

```
cd deck
npm install
npm start
```

## Presented at:

- [Droidcon Lisbon 2022][dclx]
- [Droidcon Berlin 2022][dcbln]

## Abstract

When building applications it's easy to focus on the happy path and forget about error scenarios. However, in the real world failures happen, and dealing with them is as important as the happy pah.

Kotlin and the standard library offer some approaches for dealing with errors like nullable types and sealed classes. But for many robust applications that might not be enough! I will present the tools and techniques offered by the Arrow library that builds on top and complements what Kotlin offers and helps you achieve composable error handling logic with a minimal amount of boilerplate code.

After attending this talk, you will have yet another tool in your toolbelt that will help you build robust applications.

[dclx]: https://www.lisbon.droidcon.com/session/error-handling-beyond-the-standard-library
[dcbln]: https://berlin.droidcon.com/stojan-anastasov/
