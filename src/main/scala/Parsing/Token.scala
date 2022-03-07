package parsing

class Token extends Positionable{
}

object Tokens{
  final case class KeywordToken(value: String) extends Token
  final case class IdentifierToken(name: String) extends Token

  final case class IntLitToken(value: Int) extends Token
  final case class FloatLitToken(value: Float) extends Token
  final case class StringLitToken(value: String) extends Token
  final case class BoolLitToken(value: Boolean) extends Token

  final case class DelimiterToken(value: String) extends Token
  final case class OperatorToken(name: String) extends Token
  final case class CommentToken(text: String) extends Token
  final case class SpaceToken() extends Token
  final case class ErrorToken(content: String) extends Token
  final case class EOFToken() extends Token
}
