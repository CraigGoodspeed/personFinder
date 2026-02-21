# Protection of private information

Anything that goes out onto the web is vulnerable, PII information like names imho should not be sent to an AI endpoint

if there is any way to avoid it, do so. If we have a close look at the requirement, we are asked to build a bio based

on the users job title and hobbies, this can be done without any PII information?

### Risks

We actually have no idea what is done in the background with that information and we have just told this model where
to find this person at the current time. This rings a lot of alarm bells.

I am going to feed this answer to Gemini and see how it response :smile:

It included the idea of tokenising names this is a cool idea i had not thought of....

the polished response.

Sanitization Strategy: I practiced "Data Minimization." The LLM request was constructed using only non-identifying attributes (jobTitle and hobbies). By using a dedicated Mapper to create a prompt-specific DTO, I ensured that the name and location fields never left our internal memory.

Banking Architecture: For a high-security environment, I would implement an AI Proxy Layer. This layer performs:

    PII Scrubbing: Automatically redacting sensitive patterns.

    Contextual Masking: Replacing real names with tokens.

    Private Deployment: Utilizing VPC-hosted models (like AWS Bedrock or Azure OpenAI) where data is not used for base model training and stays within the bank's security perimeter.